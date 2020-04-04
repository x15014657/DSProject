package com.conorjc.dsproject.jmdns;

import java.util.List;

/**
 * @author dominic
 */
public interface ServiceObserver {

    boolean interested(String type);

    List<String> serviceInterests();

    void serviceAdded(ServiceDescription service);

    String getName();

    void switchService(String name);

}
