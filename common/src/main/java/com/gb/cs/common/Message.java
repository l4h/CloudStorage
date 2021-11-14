package com.gb.cs.common;

import java.io.Serializable;

public interface Message extends Serializable {



    CommandType getCommand();

}
