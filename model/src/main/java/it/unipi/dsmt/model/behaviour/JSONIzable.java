package it.unipi.dsmt.model.behaviour;

public interface JSONIzable<T>{
    public void encode();
    public T decode();
}
