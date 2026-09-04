package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackListSearchThread extends Thread {

    private final String direccionIp;
    private final int servidorInicial;
    private final int servidorFinal;
    private final List<Integer> ocurrencias = new LinkedList<>();
    private final AtomicInteger contadorTotal;
    private final int limiteAlarma;

    public BlackListSearchThread(String direccionIp, int servidorInicial, int servidorFinal, AtomicInteger contadorTotal, int limiteAlarma) {
        this.direccionIp = direccionIp;
        this.servidorInicial = servidorInicial;
        this.servidorFinal = servidorFinal;
        this.contadorTotal = contadorTotal;
        this.limiteAlarma = limiteAlarma;
    }

    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        for (int i = servidorInicial; i < servidorFinal && contadorTotal.get() < limiteAlarma; i++) {
            if (skds.isInBlackListServer(i, direccionIp)) {
                ocurrencias.add(i);
                contadorTotal.incrementAndGet();
            }
        }
    }

    public List<Integer> getOcurrencias() {
        return ocurrencias;
    }
}
