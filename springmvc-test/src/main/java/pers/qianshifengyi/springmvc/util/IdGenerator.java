package pers.qianshifengyi.springmvc.util;

import java.util.UUID;

/**
 * Created by zhangshan on 17/4/20.
 */
public class IdGenerator {

    public static String generate(){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        return uuid+random(4);
    }

    private static String random(int randomCount){
        StringBuilder sb = new StringBuilder();
        for(int i=1;i<=randomCount;i++){
            sb.append((int)(Math.random()*10));
        }
        return sb.toString();
    }

}
