package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

public class BlackListSearchThread extends Thread {

    private final String direccionIp;
    private final int servidorInicial;
    private final int servidorFinal;
    private final List<Integer> ocurrencias = new LinkedList<>();

    public BlackListSearchThread(String direccionIp, int servidorInicial, int servidorFinal) {
        this.direccionIp = direccionIp;
        this.servidorInicial = servidorInicial;
        this.servidorFinal = servidorFinal;
    }

    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        for (int i = servidorInicial; i < servidorFinal; i++) {
            if (skds.isInBlackListServer(i, direccionIp)) {
                ocurrencias.add(i);
            }
        }
    }

    public List<Integer> getOcurrencias() {
        return ocurrencias;
    }
}
