package com.mygdx.game.desktop;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Марина on 02.04.2017.
 */
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
public class MyTexturePacker {

        private static final String TEXTURES_DIRECTORY = "textures";
        private static final String ATLAS_DIRECTORY = "atlas";
        private static final String ATLAS_FILE_NAME = "atlas";

        public static void main(String[] args) {
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.filterMin = Texture.TextureFilter.Linear;
            settings.filterMag = Texture.TextureFilter.Linear;

            TexturePacker.process(settings,
                    TEXTURES_DIRECTORY,
                    ATLAS_DIRECTORY,
                    ATLAS_FILE_NAME);
        }

}
