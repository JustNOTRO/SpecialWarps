package me.notro.specialwarps.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@Getter
@RequiredArgsConstructor
public class Warp {
    private final String ownerName;
    private final String warpName;
    private final Location warpLocation;
}
