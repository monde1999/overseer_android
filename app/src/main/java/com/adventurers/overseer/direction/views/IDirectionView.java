package com.adventurers.overseer.direction.views;

import com.adventurers.overseer.map.models.Location;

import java.util.List;

public interface IDirectionView {
    void renderPaths(List<List<Location>> paths);
    void renderPathFindingUnsuccessful();
}
