/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationMenuComponent.java
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

package com.noahhusby.sledgehammer.dialogs.components.setup;

import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;

public class LocationMenuComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "location";
    }

    @Override
    public String getPrompt() {
        return "Edit the server locations?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Type ", ChatColor.GRAY),
                new TextElement("list", ChatColor.RED), new TextElement(" to view current locations, ", ChatColor.GRAY),
                new TextElement("add", ChatColor.RED), new TextElement(" to add a new location, ", ChatColor.GRAY),
                new TextElement("remove", ChatColor.RED), new TextElement(" to remove an existing location, or ", ChatColor.GRAY),
                new TextElement("finish", ChatColor.RED), new TextElement(" to finish setting up this server.", ChatColor.GRAY)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("list") || vm.equals("add") || vm.equals("remove") || vm.equals("finish");
    }
}
