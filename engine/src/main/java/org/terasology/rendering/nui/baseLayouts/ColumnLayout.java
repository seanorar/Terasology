/*
 * Copyright 2013 MovingBlocks
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
package org.terasology.rendering.nui.baseLayouts;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import org.terasology.input.events.KeyEvent;
import org.terasology.input.events.MouseButtonEvent;
import org.terasology.input.events.MouseWheelEvent;
import org.terasology.math.Rect2i;
import org.terasology.math.TeraMath;
import org.terasology.math.Vector2i;
import org.terasology.math.Border;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.CoreLayout;
import org.terasology.rendering.nui.LayoutHint;
import org.terasology.rendering.nui.UIWidget;

import java.util.Iterator;
import java.util.List;

/**
 * @author Immortius
 */
public class ColumnLayout extends CoreLayout<LayoutHint> {

    private int columns = 1;
    private int horizontalSpacing;
    private int verticalSpacing;

    private List<UIWidget> widgetList = Lists.newArrayList();

    @SerializedName("column-widths")
    private float[] columnWidths = new float[] {1.0f};

    public ColumnLayout() {
    }

    public ColumnLayout(String id) {
       super(id);
    }

    public void addWidget(UIWidget widget) {
        widgetList.add(widget);
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        columnWidths = new float[columns];
        float equalWidth = 1.0f / columns;
        for (int i = 0; i < columnWidths.length; ++i) {
            columnWidths[i] = equalWidth;
        }
    }

    public void setColumnWidths(float ... widths) {
        if (widths.length > columns) {
            throw new IllegalArgumentException("More widths than columns");
        }

        float total = 0;
        int columnIndex = 0;
        while (columnIndex < widths.length) {
            total += widths[columnIndex];
            columnWidths[columnIndex] = widths[columnIndex];
            columnIndex++;
        }

        if (total > 1.0f) {
            throw new IllegalArgumentException("Total width exceeds 1.0");
        }

        if (columnIndex < columnWidths.length) {
            float remainingWidth = 1.0f - total;
            float widthPerColumn = remainingWidth / (columnWidths.length - columnIndex);
            while (columnIndex < columnWidths.length) {
                columnWidths[columnIndex++] = widthPerColumn;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!widgetList.isEmpty()) {
            Vector2i availableSize = canvas.size();
            int numRows = TeraMath.ceilToInt((float) widgetList.size() / columns);
            if (numRows > 0) {
                availableSize.y -= verticalSpacing * (numRows - 1);
            }
            if (columns > 0) {
                availableSize.x -= horizontalSpacing * (columns - 1);
            }
            Vector2i rowSize = availableSize;
            rowSize.y /= numRows;

            Vector2i currentOffset = new Vector2i();
            int currentColumn = 0;
            for (UIWidget widget : widgetList) {
                Vector2i cellSize = new Vector2i(rowSize);
                cellSize.x *= columnWidths[currentColumn];
                if (widget != null) {
                    Vector2i drawSize = new Vector2i(cellSize);

                    Rect2i drawRegion = Rect2i.createFromMinAndSize(currentOffset.x, currentOffset.y, drawSize.x, drawSize.y);
                    canvas.drawElement(widget, drawRegion);
                }

                if (++currentColumn == columns) {
                    currentColumn = 0;
                    currentOffset.x = 0;
                    currentOffset.y += cellSize.y + verticalSpacing;
                } else {
                    currentOffset.x += cellSize.x + horizontalSpacing;
                }
            }
        }
    }

    @Override
    public Vector2i calcContentSize(Canvas canvas, Vector2i areaHint) {
        Vector2i totalSize = new Vector2i();
        Vector2i rowSize = new Vector2i();

        int availableWidth = areaHint.getX() - horizontalSpacing * (columns - 1);

        int currentColumn = 0;
        for (UIWidget widget : widgetList) {
            Vector2i cellSize = new Vector2i(availableWidth, areaHint.y - totalSize.y);
            cellSize.x *= columnWidths[currentColumn];
            Vector2i contentSize = canvas.calculateSize(widget, cellSize);
            rowSize.x += contentSize.x;
            rowSize.y = Math.max(rowSize.y, contentSize.y);

            if (++currentColumn == columns) {
                currentColumn = 0;
                totalSize.x = Math.max(totalSize.x, rowSize.x);
                totalSize.y += rowSize.y + verticalSpacing;
                rowSize.set(0, 0);
            } else {
                rowSize.x += horizontalSpacing;
            }
        }
        totalSize.y -= verticalSpacing;
        return totalSize;
    }

    @Override
    public void update(float delta) {
        for (UIWidget widget : widgetList) {
            widget.update(delta);
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent event) {
    }

    @Override
    public void onMouseWheelEvent(MouseWheelEvent event) {
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
    }

    @Override
    public Iterator<UIWidget> iterator() {
        return widgetList.iterator();
    }

    @Override
    public void addWidget(UIWidget element, LayoutHint hint) {
        addWidget(element);
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }
}