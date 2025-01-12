package Table;
public class block_info {
    static public String block_name;
    static public int block_number;
    static public String start_address;
    static public String length;

    public block_info(String block_name, int block_number, String start_address, String length) {
        this.block_name = block_name;
        this.block_number = block_number;
        this.start_address = start_address;
        this.length = length;
    }
}