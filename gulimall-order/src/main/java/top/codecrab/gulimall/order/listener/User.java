package top.codecrab.gulimall.order.listener;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String age;
}
