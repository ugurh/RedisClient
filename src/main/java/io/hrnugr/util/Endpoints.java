package io.hrnugr.util;

public interface Endpoints {

    String ROOT_GEN_UNIT ="/genunit";
    String GET_GEN_UNIT_BY_ID ="/{unitId}";
    String CREATE_GEN_UNIT ="/create";
    String UPDATE_GEN_UNIT ="/update";
    String DELETE_GEN_UNIT ="/delete/{unitId}";
    String GET_ALL_GEN_UNIT = "/all";

}
