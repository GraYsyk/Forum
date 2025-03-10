package net.graysenko.com.ForumDQC.Services;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

@Service
public class SystemService {

    public final SystemInfo systemInfo = new SystemInfo();
    public final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    public final OperatingSystem os = systemInfo.getOperatingSystem();

    //CPU usage method
    public double getCPUusage() {
        CentralProcessor cpu = hardware.getProcessor();
        long[] pervTicks = cpu.getSystemCpuLoadTicks();
        Util.sleep(1000);
        double cpuLoad = cpu.getSystemCpuLoadBetweenTicks(pervTicks) * 100;
        return Math.round(cpuLoad * 100) / 100.0;
    }

    //RAM usage method
    public double getMemoryUsage() {
        GlobalMemory memory = hardware.getMemory();
        long total = memory.getTotal();
        long used = total - memory.getAvailable();
        return (double) used / total * 100;
    }

    //HARDWARE usage method
    public double getDiskUsage() {
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        return fileSystem.getFileStores().stream()
                .mapToDouble(store -> ((double) (store.getTotalSpace() - store.getUsableSpace()) / store.getTotalSpace()) * 100)
                .average()
                .orElse(-1);
    }

    //NETWORK SPEED method
    public String getNetworkSpeed(){
        return hardware.getNetworkIFs().stream()
                .map(iface -> iface.getSpeed() / 1_000_000 + " Mbps")
                .findFirst().orElse("Unknown");
    }

    //CPU temperature method
    public double getCpuTemperature(){
        return hardware.getSensors().getCpuTemperature();
    }

    public String getUpTime(){
        long uptimeSeconds = os.getSystemUptime();
        long days = uptimeSeconds / 86400;
        long hours = (uptimeSeconds % 86400) / 3600;
        long minutes = (uptimeSeconds % 3600) / 60;
        return String.format("%02d:%02d:%02d", hours, minutes, days);
    }

}
