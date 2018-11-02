import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView {
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                     int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport( numRows, numCols );
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp( this.viewport.getCol() + colDelta, 0,
                world.getNumCols() - this.viewport.getNumCols() );
        int newRow = clamp( this.viewport.getRow() + rowDelta, 0,
                world.getNumRows() - this.viewport.getNumRows() );

        viewport.shift( newCol, newRow );
    }

    public void drawBackground() {
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld( col, row );
                Optional<PImage> image = world.getBackgroundImage( worldPoint );
                if (image.isPresent()) {
                    screen.image( image.get(), col * tileWidth,
                            row * tileHeight );
                }
            }
        }
    }


    public void drawEntities() {
        for (Entity entity : world.entities()) {
            Point pos = entity.getPosition();

            if (viewport.contains( pos )) {
                Point viewPoint = this.viewport.worldToViewport( pos.x, pos.y );
                screen.image( entity.getCurrentImage(),
                        viewPoint.x * tileWidth, viewPoint.y * tileHeight );
            }
        }
    }

    public void drawViewport() {
        drawBackground();
        drawEntities();
    }

    public int clamp(int value, int low, int high) {
        return Math.min( high, Math.max( value, low ) );
    }
}
