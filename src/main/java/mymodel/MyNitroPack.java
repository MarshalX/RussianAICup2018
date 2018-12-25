package mymodel;


import model.NitroPack;

import utils.Vec3D;


public class MyNitroPack{
    private int id;
    private Vec3D position;
    private double nitroAmount;
    private Integer respawnTicks;

    public MyNitroPack(NitroPack nitroPack) {
        id = nitroPack.id;
        position = new Vec3D(nitroPack.x, nitroPack.y, nitroPack.z);
        nitroAmount = nitroPack.nitro_amount;
        respawnTicks = nitroPack.respawn_ticks;
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

    public double getNitroAmount() {
        return nitroAmount;
    }

    public void setNitroAmount(double nitroAmount) {
        this.nitroAmount = nitroAmount;
    }

    public Integer getRespawnTicks() {
        return respawnTicks;
    }

    public void setRespawnTicks(Integer respawnTicks) {
        this.respawnTicks = respawnTicks;
    }
}
