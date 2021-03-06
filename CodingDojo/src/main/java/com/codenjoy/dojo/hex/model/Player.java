package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    List<Hero> heroes;
    private Hero active;
    private Field field;
    Hero newHero;

    public Player(EventListener listener, Field field) {
        this.listener = listener;
        this.field = field;
        heroes = new LinkedList<Hero>();
        clearScore();
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(SampleEvents event) {
        switch (event) {
            case LOOSE: gameOver(); break;
            case WIN: increaseScore(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        score = 0;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

    public void newHero() {
        Point pt = field.getFreeRandom();
        Hero hero = new Hero(pt);
        hero.init(field);
        heroes.clear();
        heroes.add(hero);
    }

    public void addHero(Hero newHero) {
        this.newHero = newHero;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                if (active == null) return;
                active.down();
            }

            @Override
            public void up() {
                if (active == null) return;
                active.up();
            }

            @Override
            public void left() {
                if (active == null) return;
                active.left();
            }

            @Override
            public void right() {
                if (active == null) return;
                active.right();
            }

            @Override
            public void act(int... p) {
                int x = p[0]; // TODO validation
                int y = p[1];

                Hero hero = field.getHero(x, y);
                if (hero != null && heroes.contains(hero)) {
                    active = hero;
                }
            }
        };
    }

    public boolean isAlive() {
        return true; // TODO
    }

    public void remove(Hero hero) {
        if (newHero == hero) {
            newHero = null;
        }
    }

    public void applyNew() {
        if (newHero != null) {
            heroes.add(newHero);
            newHero = null;
        }
    }
}