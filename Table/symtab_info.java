package Table;

public class symtab_info {
    public String label_name;
    public String label_address;
    public String block;

    public symtab_info(String label_name, String label_address, String block) {
        this.label_name = label_name;
        this.label_address = label_address;
        this.block = block;
    }
}
