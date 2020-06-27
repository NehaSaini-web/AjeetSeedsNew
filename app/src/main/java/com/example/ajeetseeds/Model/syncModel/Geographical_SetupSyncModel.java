package com.example.ajeetseeds.Model.syncModel;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.Geographical_SetupTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.RegionMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.ZoneMasterTable;

import java.util.List;

public class Geographical_SetupSyncModel {
    public List<Geographical_SetupTable.Geographical_Setup> geographical_Setup;
    public List<ZoneMasterTable.ZoneMaster> zone_master;
    public List<StateMasterTable.StateMaster> state_master;
    public List<RegionMasterTable.RegionMaster> region_master;
    public List<DistrictMasterTable.DistrictMaster> district_master;
    public List<TalukaMasterTable.TalukaMaster> taluka_master;
}
