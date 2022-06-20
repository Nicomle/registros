package app.enums;

public enum Ranges {
    USUARIO("USUARIO"),
    ADMINISTRADOR("ADMINISTRADOR");

    private String range;

    Ranges(String range){
        this.range = range;
    }

    public static boolean validate(String range){
        Ranges[] rangos = Ranges.values();
        for(int i=0;i<rangos.length;i++) {
            if(rangos[i].toString().equals(range)) {
                return true;
            }
        }
        return false;
    }
}
