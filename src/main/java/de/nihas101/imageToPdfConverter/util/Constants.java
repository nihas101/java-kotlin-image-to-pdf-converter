/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.imageToPdfConverter.util;

import javafx.scene.image.Image;

import java.io.IOException;

import static javafx.embed.swing.SwingFXUtils.toFXImage;
import static javax.imageio.ImageIO.read;

public class Constants {
    public final static Constants RESOURCES = new Constants();
    public final static Double CELL_SIZE = 80.0;
    public final static int LIST_CELL_MAX_STRING_LENGTH = 27;
    public final static int NOTIFICATION_MAX_STRING_LENGTH = 40;
    public final static Float NO_MARGIN = 0F;

    public final static int IMAGE_DISPLAY_MAX_WIDTH = 1600;
    public final static int IMAGE_DISPLAY_MAX_HEIGHT = 900;

    public final static int IMAGE_MAP_MAX_SIZE = 100;

    /* Source: icons8.com/icon/2828/open */
    private final static String DIRECTORY_IMAGE_PATH = "/icons8-open-500.png";
    /* Source: icons8.com/icon/54151/gear */
    public final static String GEAR_IMAGE_PATH = "/icons8-open-500.png";
    public Image DIRECTORY_IMAGE_FILE;

    private Constants() {
        try {
            DIRECTORY_IMAGE_FILE = toFXImage(read(getClass().getResource(DIRECTORY_IMAGE_PATH)), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
