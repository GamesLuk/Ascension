/*
 * This file is part of DiscordSRV, licensed under the GPLv3 License
 * Copyright (c) 2016-2025 Austin "Scarsz" Shapiro, Henri "Vankka" Schubin and DiscordSRV contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.discordsrv.fabric.integration;

import com.discordsrv.fabric.FabricDiscordSRV;

import java.util.UUID;

/**
 * Integration helper for Servers Link mod.
 * Detects if a player is being transferred between servers in a network.
 */
public class ServersLinkIntegration {

    private static boolean available = false;
    private static boolean checked = false;

    /**
     * Checks if Servers Link is available.
     */
    public static boolean isAvailable() {
        if (!checked) {
            try {
                Class.forName("io.github.kgriff0n.api.ServersLinkApi");
                available = true;
                System.out.println("[DiscordSRV] Servers Link gefunden - Transfer-Detection aktiviert");
            } catch (ClassNotFoundException e) {
                available = false;
                System.out.println("[DiscordSRV] Servers Link nicht gefunden - Standard Join/Leave Nachrichten");
            }
            checked = true;
        }
        return available;
    }

    /**
     * Checks if a player is currently being transferred to another server.
     *
     * @param uuid The player's UUID
     * @return true if the player is being transferred, false otherwise
     */
    public static boolean isPlayerBeingTransferred(UUID uuid) {
        if (!isAvailable()) {
            return false;
        }

        try {
            return io.github.kgriff0n.api.ServersLinkApi.isPlayerBeingTransferred(uuid);
        } catch (Throwable t) {
            // Fallback if something goes wrong
            return false;
        }
    }

    /**
     * Checks if a player join is from a transfer (not a real network join).
     *
     * @param uuid The player's UUID
     * @return true if the player joined via transfer, false otherwise
     */
    public static boolean isPlayerJoiningFromTransfer(UUID uuid) {
        if (!isAvailable()) {
            return false;
        }

        try {
            // Check if player is in the preventConnect set (Servers Link puts transferring players there)
            return io.github.kgriff0n.api.ServersLinkApi.getPreventConnect().contains(uuid);
        } catch (Throwable t) {
            return false;
        }
    }
}

