package de.netpacket.nettyrestful;

import de.netpacket.nettyrestful.interfaces.Route;

public class RestBuilder {


    private String address;
    private int port;

    private Route route;
    private NettyServer nettyServer;

    public RestBuilder setAddress(String address, int port) {
        this.address = address;
        this.port = port;
        return this;
    }

    public RestBuilder setRoute(Route route) {
        this.route = route;
        return this;
    }

    public RestBuilder build() {
        this.nettyServer = new NettyServer(address == null ? "127.0.0.1" : address, port == 0 ? 3511 : port, route);
        return this;
    }

    public void connect(){
        new Thread(() -> this.nettyServer.connect()).start();
    }

}
