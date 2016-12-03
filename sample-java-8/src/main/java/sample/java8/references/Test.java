package sample.java8.references;

public class Test {

    public static enum SslAuthenticationMode {
        ONEWAY("1-way"), 
        TWOWAY("2-way"), 
        ANONYMOUS("anonymous");
        private final String allowedValue;

        /**
         * Returns the allowed value for the management model.
         * 
         * @return the allowed model value
         */
        public String getAllowedValue() {
                return allowedValue;
        }

        SslAuthenticationMode(String allowedValue) {
                this.allowedValue = allowedValue;
        }

        @Override
        public String toString() {
                return allowedValue;
        }
}
    
}
