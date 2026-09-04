/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int n){

        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        int totalServidores=skds.getRegisteredServersCount();
        int tamañoBase=totalServidores/n;
        int servidoresSobrantes=totalServidores%n;

        BlackListSearchThread[] hilos=new BlackListSearchThread[n];
        AtomicInteger contadorTotal=new AtomicInteger(0);

        int inicio=0;
        for (int i=0;i<n;i++){
            int tamañoSegmento=tamañoBase+(i<servidoresSobrantes?1:0);
            int fin=inicio+tamañoSegmento;

            hilos[i]=new BlackListSearchThread(ipaddress, inicio, fin, contadorTotal, BLACK_LIST_ALARM_COUNT);
            hilos[i].start();

            inicio=fin;
        }

        for (BlackListSearchThread hilo:hilos){
            try {
                hilo.join();
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            blackListOcurrences.addAll(hilo.getOcurrencias());
        }

        int ocurrencesCount=contadorTotal.get();

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{totalServidores, skds.getRegisteredServersCount()});

        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
