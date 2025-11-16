
package com.mycompany.app;

import javax.swing.SwingUtilities;
import java.io.IOException;

/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

public class App {
    
    public static void main(String[] args) {
      
        // Iniciar servidor HTTP embebido (para uso "online")
        CalculadoraServer server = new CalculadoraServer();
        try {
            server.start(8000);
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor HTTP: " + e.getMessage());
        }
      
        /*
        SwingUtilities.invokeLater(() -> {
            MainGui.createAndShow();
        });
    }
         */
}   
}   
