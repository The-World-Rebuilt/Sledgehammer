/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerConfig.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.config.types.SledgehammerServer;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.network.P2S.P2SInitializationPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerConfig {
    private static ServerConfig instance;

    public static ServerConfig getInstance() {
        return instance;
    }

    public static void setInstance(ServerConfig instance) {
        ServerConfig.instance = instance;
    }

    public ServerConfig() { }

    @Expose(serialize = true, deserialize = true)
    public List<SledgehammerServer> servers = new ArrayList<>();

    public LinkedList<ServerInfo> bungeeServers = Lists.newLinkedList();

    public List<SledgehammerServer> getServers() {
        return servers;
    }

    public void onServerJoin(ServerConnectedEvent e) {
        SledgehammerServer s = getServer(e.getServer().getInfo().getName());
        if(s != null) {
            if(!s.isInitialized()) {
                SledgehammerNetworkManager.getInstance().sendPacket(new P2SInitializationPacket(e.getPlayer().getName(), e.getServer().getInfo().getName()));
            }
        }
    }

    public void initializeServer(ServerInfo serverInfo, JSONObject data) {
        SledgehammerServer s = getServer(serverInfo.getName());
        if(s != null) {
            s.initialize((String) data.get("version"), (String) data.get("tpllmode"));
            pushServer(s);
        }
    }

    public LinkedList<ServerInfo> getBungeeServers() {
        if(bungeeServers.isEmpty()) {
            Map<String, ServerInfo> serversTemp = ProxyServer.getInstance().getServers();
            for(Map.Entry<String, ServerInfo> s : serversTemp.entrySet()) {
                bungeeServers.add(s.getValue());
            }
        }
        return bungeeServers;
    }

    public void pushServer(SledgehammerServer server) {
        List<SledgehammerServer> remove = new ArrayList<>();
        for(SledgehammerServer s : servers) {
            if(s.name.toLowerCase().equals(server.name.toLowerCase())) {
                remove.add(s);
            }
        }

        for(SledgehammerServer s : remove) {
            servers.remove(s);
        }

        servers.add(server);
        if(!server.getServerInfo().getPlayers().isEmpty() && !server.isInitialized()) {
            ProxiedPlayer player = server.getServerInfo().getPlayers().toArray(new ProxiedPlayer[
                    server.getServerInfo().getPlayers().size()])[0];
            SledgehammerNetworkManager.getInstance().sendPacket(new P2SInitializationPacket(player.getName(), player.getServer().getInfo().getName()));
        }
        ConfigHandler.getInstance().saveServerDB();
    }

    public void removeServer(SledgehammerServer server) {
        servers.remove(server);
        ConfigHandler.getInstance().saveServerDB();
    }


    public SledgehammerServer getServer(String name) {
        for(SledgehammerServer s : servers) {
            if(s.name.toLowerCase().equals(name.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    public List<Location> getLocationsFromServer(String server) {
        for(SledgehammerServer s : servers) {
            if(s.name.toLowerCase().equals(server.toLowerCase())) {
                return s.locations;
            }
        }

        return null;
    }
}
