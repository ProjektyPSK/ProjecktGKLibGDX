package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Main;
import com.mygdx.game.sprites.Blaster;
import com.mygdx.game.sprites.EnemyBlaster;
import com.mygdx.game.sprites.EnemyShip;
import com.mygdx.game.sprites.Ship;
import com.mygdx.game.sprites.Upgrade;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {

            case Main.SHIP_ENEMY_BIT | Main.SHIP_HERO_BIT:
                if(fixA.getFilterData().categoryBits == Main.SHIP_ENEMY_BIT){

                   ((EnemyShip) fixA.getUserData()).colideWithEntiti();

                }
                else if (fixB.getFilterData().categoryBits == Main.SHIP_ENEMY_BIT){

                     ((EnemyShip) fixB.getUserData()).colideWithEntiti();
                }
                break;

            case Main.SHIP_ENEMY_BIT | Main.BLASTER_HERO:
                if(fixA.getFilterData().categoryBits == Main.SHIP_ENEMY_BIT ) {
                    ((EnemyShip) fixA.getUserData()).colideWithEntiti();
                    ((Blaster) fixB.getUserData()).colideWithEntiti();
                }
                else if (fixB.getFilterData().categoryBits == Main.SHIP_ENEMY_BIT ) {
                    ((EnemyShip) fixB.getUserData()).colideWithEntiti();
                    ((Blaster) fixA.getUserData()).colideWithEntiti();
                }
                break;

            case Main.SHIP_HERO_BIT | Main.BLASTER_ENEMY:
                if(fixA.getFilterData().categoryBits == Main.SHIP_HERO_BIT) {
                    ((EnemyBlaster) fixB.getUserData()).colideWithEntiti();
                    ((Ship) fixA.getUserData()).colideWithEntiti();
                }
                else if(fixB.getFilterData().categoryBits == Main.SHIP_HERO_BIT) {
                    ((EnemyBlaster) fixA.getUserData()).colideWithEntiti();
                    ((Ship) fixB.getUserData()).colideWithEntiti();
                }
                break;

            case Main.SHIP_ENEMY_BIT | Main.SHIP_ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Main.SHIP_HERO_BIT) {
                    ((EnemyShip) fixB.getUserData()).colideWithEntiti();
                    ((Ship) fixA.getUserData()).colideWithEntiti();
                }
                else if(fixB.getFilterData().categoryBits == Main.SHIP_HERO_BIT) {
                    ((EnemyShip) fixA.getUserData()).colideWithEntiti();
                    ((Ship) fixB.getUserData()).colideWithEntiti();
                }
                break;

            case Main.SHIP_HERO_BIT | Main.UPGRADE_BIT:
                if(fixA.getFilterData().categoryBits == Main.SHIP_HERO_BIT) {
                    ((Upgrade) fixB.getUserData()).colideWithEntiti();
                    ((Ship) fixA.getUserData()).colideWithUpgrade();
                }
                else if(fixB.getFilterData().categoryBits == Main.SHIP_HERO_BIT) {
                    ((Upgrade) fixA.getUserData()).colideWithEntiti();
                    ((Ship) fixB.getUserData()).colideWithUpgrade();
                }
                break;


            default:
                break;



        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
