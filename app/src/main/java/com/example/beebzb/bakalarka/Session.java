package com.example.beebzb.bakalarka;

public final class Session {

        private static final Session INSTANCE = new Session();

    private Session() {
            if (INSTANCE != null) {
                throw new IllegalStateException("Already instantiated");
            }
        }

        public static Session getInstance() {
            return INSTANCE;
        }

}
