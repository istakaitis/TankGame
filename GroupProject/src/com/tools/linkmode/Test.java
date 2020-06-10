package com.tools.linkmode;

public class Test {

    public static void main(String[] args) {
        SSHService s = new SSHService();
        User user = new User();
        Statistics ss = new Statistics();

        user.setUsername("ignas");
        user.setPassword("asdf");
        user.setLogin_attempts(0);

        ss.setUsername("ignas");
        ss.setGames_played(0);
        ss.setWins(0);
        ss.setLosses(0);
        ss.setTotal_kills(0);
        ss.setTotal_deaths(0);
        ss.setKill_death_ratio();
        ss.setWin_rate();

        //s.insertUser(user);
        s.updateStatistics(ss);
//        s.updateUser(user);
//        s.deleteUser("tttt");
//        s.deleteStatistics("tttt");

//        user = s.selectUser("tttt");
//        System.out.println(user.toString());
//
//        ss = s.selectStatistics("tttt");
//        System.out.println(ss.toString());
    }
}


