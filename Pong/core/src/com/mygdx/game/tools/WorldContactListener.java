package com.mygdx.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.PongGame;
import com.mygdx.game.game_objs.Ball;
import com.mygdx.game.game_objs.Paddle;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        float midPadding = 0.05f;

        Ball ball;
        Paddle paddle;

        if(fixA.getUserData() instanceof Ball){
            ball = (Ball) fixA.getUserData();
            paddle = (Paddle) fixB.getUserData();
        }else{
            // fixB is ball
            ball = (Ball) fixB.getUserData();
            paddle = (Paddle) fixA.getUserData();
        }

        // N: first refers to max, second is min
        // check top
        if(ball.getBounds().y <= paddle.getBounds().y + paddle.getBounds().getHeight() &&
                ball.getBounds().y >= paddle.getBounds().y + midPadding){
            ball.bounce("top");
        }else if(ball.getBounds().y < paddle.getBounds().y + midPadding &&
                ball.getBounds().y >= paddle.getBounds().y - midPadding){
            // mid
            ball.bounce("mid");
        }else if(ball.getBounds().y < paddle.getBounds().y - midPadding &&
                ball.getBounds().y >= paddle.getBounds().y - paddle.getBounds().getHeight()) {
            // bottom
            ball.bounce("bottom");
        }
//        if(fixA.getUserData() instanceof Ball && fixB.isSensor()){
//            Ball ball = (Ball) fixA.getUserData();
//
//            // this means fixB is the paddle
//            if(fixB.getUserData().equals("top")){
//                // hits the top
//                ball.bounce("top");
//            }else if(fixB.getUserData().equals("mid")){
//                // hits the middle
//                ball.bounce("mid");
//            }else if(fixB.getUserData().equals("bottom")){
//                // hits the bottomm
//                ball.bounce("bottom");
//            }
//        }else if(fixB.getUserData() instanceof Ball && fixA.isSensor()){
//            // fixB is ball
//            Ball ball = (Ball) fixB.getUserData();
//
//            // this means fixA is the paddle
//            if(fixA.getUserData().equals("top")){
//                // hits the top
//                ball.bounce("top");
//            }else if(fixA.getUserData().equals("mid")){
//                // hits the middle
//                ball.bounce("mid");
//            }else if(fixA.getUserData().equals("bottom")){
//                // hits the bottomm
//                ball.bounce("bottom");
//            }
//        }
    }


//    @Override
//    public void beginContact(Contact contact) {
//        Fixture fixA = contact.getFixtureA();
//        Fixture fixB = contact.getFixtureB();
//
//        // OR adding (used to tell which fixtures collided)
//        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
//
//        switch(cDef){
//            case PongGame.PADDLE_TOP_BIT | PongGame.BALL_BIT:
//                if(fixA.getFilterData().categoryBits == PongGame.BALL_BIT)
//                    ((Ball) fixA.getUserData()).bounce("top");
//                else
//                    ((Ball) fixB.getUserData()).bounce("top");
//                break;
//            case PongGame.PADDLE_MID_BIT | PongGame.BALL_BIT:
//                if(fixA.getFilterData().categoryBits == PongGame.BALL_BIT)
//                    ((Ball) fixA.getUserData()).bounce("mid");
//                else
//                    ((Ball) fixB.getUserData()).bounce("mid");
//                break;
//            case PongGame.PADDLE_BOTTOM_BIT | PongGame.BALL_BIT:
//                if(fixA.getFilterData().categoryBits == PongGame.BALL_BIT)
//                    ((Ball) fixA.getUserData()).bounce("bottom");
//                else
//                    ((Ball) fixB.getUserData()).bounce("bottom");
//                break;
//        }
//    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Ball){
            Ball ball = (Ball) fixA.getUserData();

            // toggle the bounce
            if(ball.canBounce){
                ball.canBounce = false;
            }else{
                ball.canBounce = true;
            }

        }else{
            // fixB is ball
            Ball ball = (Ball) fixB.getUserData();

            // toggle the bounce
            if(ball.canBounce){
                ball.canBounce = false;
            }else{
                ball.canBounce = true;
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
