package com.github.blarosen95.mywarpsgui.Data;

import com.github.blarosen95.mywarpsgui.MyWarpsGUI;
import com.github.blarosen95.mywarpsgui.Util.MyWarpsParser;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class SQLiteDatabase {
    private static Connection con;
    private static boolean hasData = false;
    private static File dataFolder = MyWarpsGUI.getInstance().getDataFolder();
    private static String warpsDBFile = dataFolder.getAbsolutePath() + File.separator + "MyWarpsGUI.db";

    private void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", warpsDBFile));
        initialize();
    }

    private void initialize() throws SQLException {
        if (!hasData) {
            hasData = true;

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT NAME FROM sqlite_master WHERE type='table' AND name='warps'");

            if (!resultSet.next()) {
                System.out.println("Building the warps table by parsing the YAML file from the MyWarps plugin folder.");
                Statement statement1 = con.createStatement();

                statement1.execute("CREATE TABLE warps(" +
                        "warp_id INTEGER PRIMARY KEY AUTOINCREMENT," + "warp_name VARCHAR(32)," +
                        "warp_category VARCHAR(5)," + "creator_uuid VARCHAR(36),"
                        + "essentials_warp_file VARCHAR(35));");

                //We need to add constraints to the warp_category column, it should (currently) only accept values of: town, farm, or shop (adding in an "other" category for now)
                //Statement constraintStatement = con.createStatement();
                //constraintStatement.execute("ALTER TABLE warps ADD \"CONSTRAINT\" validCategoryConstraint CHECK (warp_category IN ('Town', 'Farm', 'Shop', 'Other') );");
                //We can ensure the usage of proper values for Category when they are chosen in the menu. (todo)

                try {
                    MyWarpsParser myWarpsParser = new MyWarpsParser();
                    ArrayList<Warp> warps = myWarpsParser.getWarpList();
                    for (Warp warp : warps) {
                        //TODO: 10/4/2018: ensure that warp_id is autogenerated
                        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO warps (warp_name, warp_category, creator_uuid, essentials_warp_file) VALUES(?,?,?,?);");
                        preparedStatement.setString(1, warp.getName());
                        preparedStatement.setString(2, warp.getCategory());
                        preparedStatement.setString(3, warp.getCreatorUUID());
                        preparedStatement.setString(4, warp.getEssentialsFile());
                        preparedStatement.execute();
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                    //TODO: 10/4/2018: should we set hasData back to false here?

                }
            }
        }
    }

    // TODO: 10/4/2018: this method can only be called if:
    // (todo) the command sender's UUID.toString().equals(oldWarp.getCreatorUUID),
    // (todo) OR the command sender has the right permissions to update categories of other's warps
    public void updateCategory(Warp oldWarp, Warp newWarp) throws SQLException, ClassNotFoundException {
        if (con == null) {
            getConnection();
        }

        boolean warpExists = false;
        PreparedStatement psQuery = con.prepareStatement("SELECT * FROM warps WHERE warp_name=? AND creator_uuid=?");
        psQuery.setString(1, oldWarp.getName());
        psQuery.setString(2, oldWarp.getCreatorUUID());
        ResultSet rs = psQuery.executeQuery();
        if (rs.next()) {
            warpExists = true;
        }

        if (warpExists) {
            PreparedStatement prep = con.prepareStatement("UPDATE warps SET warp_category=? WHERE warp_name=? AND creator_uuid=?");
            prep.setString(1, newWarp.getCategory());
            prep.setString(2, oldWarp.getName());
            prep.setString(3, oldWarp.getCreatorUUID());
            prep.execute();
        }
    }

    // TODO: 10/4/2018: this method can only be called if:
    // (todo) the command sender's UUID.toString().equals(warp.getCreatorUUID),
    // (todo) OR the command sender has the right permissions to delete other's warps
    // TODO: 10/4/2018 (Successful) calls to this command should fund $500 to the warp's creator.
    // (todo) Unsuccessful calls should inform the command sender to such.
    public boolean deleteWarp(Warp warp) throws SQLException, ClassNotFoundException {
        if (con == null) {
            getConnection();
        }

        boolean warpExists = false;
        PreparedStatement psQuery = con.prepareStatement("SELECT * FROM warps WHERE warp_name=? AND creator_uuid=?");
        psQuery.setString(1, warp.getName());
        psQuery.setString(2, warp.getCreatorUUID());
        ResultSet rs = psQuery.executeQuery();
        if (rs.next()) {
            warpExists = true;
        }

        if (warpExists) {
            PreparedStatement prep = con.prepareStatement("DELETE FROM warps WHERE warp_name=? AND creator_uuid=?");
            prep.setString(1, warp.getName());
            prep.setString(2, warp.getCreatorUUID());
            prep.execute();
            return true;
        }
        return false;
    }

    /**
     * Used for finding all warps in a given category
     *
     * @param cat an integer representing five (5) possible options:
     *            1: All
     *            2: Town
     *            3: Shop
     *            4: Farm
     *            5: Other
     * @return a ResultSet containing the results of the query.
     */
    public ResultSet getWarpsInCategory(int cat) throws SQLException {
        PreparedStatement prepQuery = con.prepareStatement("SELECT * FROM warps WHERE warp_category=?");

        switch (cat) {
            case 1:
                PreparedStatement queryAll = con.prepareStatement("SELECT * FROM warps");
                return queryAll.executeQuery();
            case 2:
                prepQuery.setString(1, "Town");
                return prepQuery.executeQuery();
            case 3:
                prepQuery.setString(1, "Shop");
                return prepQuery.executeQuery();
            case 4:
                prepQuery.setString(1, "Farm");
                return prepQuery.executeQuery();
            case 5:
                prepQuery.setString(1, "Other");
                return prepQuery.executeQuery();
        }
        //If one of the above cases did not return our ResultSet, then the value of cat wasn't a valid option
        return null; // TODO: 10/5/2018 calls to this method should check if null was returned, and if so, they should log an error.
    }
}