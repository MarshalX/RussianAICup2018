package mymodel;


import model.NitroPack;

import utils.Vec3D;


public class MyNitroPack{
    private int id;
    private Vec3D position;
    private double nitro_amount;
    private Integer respawn_ticks;

    public MyNitroPack() {
        id = 0;
        position = new Vec3D();
        nitro_amount = 0;
        respawn_ticks = 0;
    }

    public MyNitroPack(NitroPack nitroPack) {
        id = nitroPack.id;
        position = new Vec3D(nitroPack.x, nitroPack.y, nitroPack.z);
        nitro_amount = nitroPack.nitro_amount;
        respawn_ticks = nitroPack.respawn_ticks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vec3D getPosition() {
        return position;
    }

    public void setPosition(Vec3D position) {
        this.position = position;
    }

    public double getNitro_amount() {
        return nitro_amount;
    }

    public void setNitro_amount(double nitro_amount) {
        this.nitro_amount = nitro_amount;
    }

    public Integer getRespawn_ticks() {
        return respawn_ticks;
    }

    public void setRespawn_ticks(Integer respawn_ticks) {
        this.respawn_ticks = respawn_ticks;
    }
}
