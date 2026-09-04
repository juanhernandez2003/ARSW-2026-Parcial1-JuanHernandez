/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv=new HostBlackListsValidator();

        String[] ips={"200.24.34.55","202.24.34.55","212.24.24.55"};

        for (String ip:ips){
            List<Integer> blackListOcurrences=hblv.checkHost(ip, 4);
            System.out.println(ip+" -> found in: "+blackListOcurrences);
        }
    }
    
}
