/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.nui;

import org.terasology.asset.AssetUri;
import org.terasology.entitySystem.event.Event;
import org.terasology.module.sandbox.API;
import org.terasology.network.OwnerEvent;

/**
 * The event is sent to the UI layer
 *
 * @author Florian <florian@fkoeberle.de>
 */
@OwnerEvent
@API
public class ScreenLayerClosedEvent implements Event {
    private AssetUri closedScreenUri;

    // Default constructor for serialization
    ScreenLayerClosedEvent() {
    }

    public ScreenLayerClosedEvent(AssetUri closedScreenUri) {
        this.closedScreenUri = closedScreenUri;
    }

    public AssetUri getClosedScreenUri() {
        return closedScreenUri;
    }
}
