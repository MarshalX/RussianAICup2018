import com.google.gson.Gson;

import model.*;
import mymodel.*;

import utils.Simulation;
import utils.Vec3D;


class Sphere {
    JsonSphere Sphere;

    public Sphere(JsonSphere jsonBall) {
        Sphere = jsonBall;
    }
}

class JsonSphere {
    double x;
    double y;
    double z;

    double radius = 1;

    double r = 1.0;
    double g = 0.0;
    double b = 0.0;

    double a = 0.5;

    public JsonSphere(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

public final class MyStrategy implements Strategy {
    Gson gson = new Gson();
    Sphere[] path = new Sphere[200];

    @Override
    public void act(Robot me, Rules rules, Game game, Action action) {
        MyRules myRules = new MyRules(rules);
        MyGame myGame = new MyGame(game, rules);

        Vec3D ballPos;

        Simulation sim = new Simulation(myRules, myGame);
        for (int i = 0; i < 200; ++i) {
            sim.tick(5);

            ballPos = myGame.getBall().getPosition();
            path[i] = new Sphere(new JsonSphere(ballPos.getX(), ballPos.getY(), ballPos.getZ()));
        }
    }
    
    @Override
    public String customRendering() {
        return gson.toJson(path);
    }
}
