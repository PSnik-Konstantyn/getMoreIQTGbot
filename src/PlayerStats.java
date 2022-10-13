public class PlayerStats implements Comparable<PlayerStats> {

    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getIQCounter() {
        return IQCounter;
    }

    public void setIQCounter(Integer IQCounter) {
        this.IQCounter = IQCounter;
    }

    private Integer IQCounter;

    @Override
    public int compareTo(PlayerStats employee) {
        return (int)(this.IQCounter - employee.getIQCounter());
    }
}
