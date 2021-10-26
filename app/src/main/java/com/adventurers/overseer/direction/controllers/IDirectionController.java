package com.adventurers.overseer.direction.controllers;

import com.adventurers.overseer.direction.models.DirectionData;
import com.adventurers.overseer.map.models.Location;

public interface IDirectionController {
    DirectionData findPath(Location currentLocation, Location goal);
}
