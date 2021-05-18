package it.unipi.dsmt.das.model.behaviour;

import com.ericsson.otp.erlang.OtpErlangObject;

import java.util.Collection;

import java.util.HashMap;

public interface Erlangizable <E extends OtpErlangObject> {
    public default E erlangize() throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }

    public default void derlangize(E erlangdata) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }
}
