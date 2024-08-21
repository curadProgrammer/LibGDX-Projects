package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundComponent implements Component {
    public HashMap<String, Sound> soundEffects = new HashMap<>();
}
