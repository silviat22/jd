package com.example.jd.s14.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jd.s14.ClassicMain;

public class MainRegion {
    private static final Logger log = LogManager.getLogger(ClassicMain.class);

    public static void main(String[] args) {
        RegionDao rd = new RegionDao();

        // create...
        final long id = 22;
        Region r = new Region(id, "Papuasia");
        rd.save(r);

        // ...then get it - creo il 2° oggetto regione //legacyGet: se non trova, ritorna un null 
        Region twentyTwo = rd.legacyGet(id);
        if (twentyTwo == null) {
            log.error("Unexpected! Can't get the region " + id);
            System.out.println("Region has not been saved correctly!");
            return;
        } else {
            System.out.println("Get: " + twentyTwo);
        }

        // rename the region
        twentyTwo.setName("Oceania");
        rd.update(twentyTwo);
        System.out.println("Update renamed: " + twentyTwo);

        // delete the region
        rd.delete(id);

        // ensure the region is actually removed from the database
        twentyTwo = rd.legacyGet(id);
        if (twentyTwo == null) {
            System.out.println("Region correctly removed");
        } else {
            System.out.println("Unexpected! Region is still alive: " + twentyTwo);
        }

        System.out.println("All regions");
        List<Region> regions = rd.getAll();
        for (Region region : regions) {
            System.out.println(region);
        }
    }
}
