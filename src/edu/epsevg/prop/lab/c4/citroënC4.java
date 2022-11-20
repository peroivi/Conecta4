/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.epsevg.prop.lab.c4;

import java.util.ArrayList;

/**
 *
 * @author Pere Roca i Villanueva , Xavier Bermejo Sotillo 
 */
public class citroënC4 implements Jugador, IAuto{
    private String nom;
    private int color=1;
    private int prof;
    private int numNodes;
    private int result_heur;
    private int heur = 0;
    private int num_tirades = 0;
    
    public int[][] taula_posibilitat = {
        {3, 4, 5, 7, 7, 5, 4, 3},
        {4, 6, 8,10,10, 8, 6, 4},
        {5, 8,11,13,13,11, 8, 5},
        {7,10,13,16,16,13,10, 7},
        {7,10,13,16,16,13,10, 7},
        {5, 8,11,13,13,11, 8, 5},
        {4, 6, 8,10,10, 8, 6, 4},
        {3, 4, 5, 7, 7, 5, 4, 3}
    };
    
    /**
     * Constructor del millor coche del mon
     * @param prof profunditat maxima a la que s'explorarà
     */
    public citroënC4(int prof) {
        this.nom = "citroënC4";
        this.prof = prof;    
        numNodes = 0;
      }
    
    /**
     * Funcio per saber el nom del jugador
     * @return el nom del jugador
     */
    @Override
    public String nom() {
        return nom;
    }
    
    /**
     * Funcio que indica el proxim moviment que farem
     * @param t taulell sobre el que es juga
     * @param color color que fa la tirada
     * @return retorna la columna on s'ha de fer la tirada
     */
    @Override
    public int moviment(Tauler t, int color) {
        this.color = color;
        int tirada = nova_tirada(t,prof);
        System.out.println("Nodes total = "+ numNodes);
        this.num_tirades++;
        return tirada;
    }
     
    /**
     * Funcion que indica la millor tirada que podem fer segons la nostre heuristica
     * @param t taulell sobre el que es juga
     * @param profunditat profunditat maxima a la que s'explorara
     * @return retorna la millor columna on fer la proxima tirada
     */
    public int nova_tirada(Tauler t, int profunditat) {
        this.heur = 0;
        int millor_heur = Integer.MIN_VALUE;
        int millor_columna = -1;
        for (int i = 0; i < t.getMida(); i++) {
            int alpha = Integer.MIN_VALUE;
            if (t.movpossible(i)) {
                Tauler tauler_aux = new Tauler(t);
                tauler_aux.afegeix(i, this.color);
                if (!tauler_aux.solucio(i, this.color)) {
                    //Descomentar si es vol sense poda
                    //alpha = minimitza(tauler_aux, profunditat - 1);
                    
                    //Comentar si es vol sense poda
                    alpha = minimitza(tauler_aux, profunditat - 1, millor_heur, Integer.MAX_VALUE);
                    
                    if (alpha > millor_heur || millor_columna == -1) {
                        millor_columna = i;
                        millor_heur = alpha;
                    }
                }
                else {
                    return i;
                }
            }
         }
        this.heur += this.result_heur;
        System.out.println("Heuristica de la tirada "+this.num_tirades+": "+this.heur);
        return millor_columna;
    }
    

    
    /**
     * Funcion que ens indica l'heuristica mes gran trobada per totes les tirades analitzades.
     * @param t taulell sobre el que s'esta jugant
     * @param profunditat profunditat fins la que s'explorara l'arbre de posibilitats
     * @param alpha valor heuristic mes alt trobat fins al moment per fer la poda
     * @param beta valor heuristic mes baix trobat fins al moment per fer la poda
     * @return retorna la heuristica mes alta de totes les tirades analitzades
     */
    
    //Descomentar si es vol sense poda
    //public int maximitza (Tauler t,int profunditat){
    //Comentar si es vol sense poda
    public int maximitza (Tauler t,int profunditat,int alpha,int beta){
        if (profunditat <= 0 || !(t.espotmoure())) {
            return heur(t);
        }
        
        int nova_alpha = Integer.MIN_VALUE;
        for (int i = 0; i < t.getMida(); i++) {
            if (t.movpossible(i)) {
                Tauler taulell_aux = new Tauler(t);
                taulell_aux.afegeix(i, this.color);
                if (!taulell_aux.solucio(i, this.color)) {
                    //Descomentar si es vol sense poda
                    //nova_alpha = Math.max(nova_alpha, minimitza(taulell_aux, profunditat - 1));
                    
                    //---Comentar si es vol sense poda-----------------
                    
                    nova_alpha = Math.max(nova_alpha, minimitza(taulell_aux, profunditat - 1, alpha, beta));
                    alpha = Math.max(nova_alpha, alpha);
                    if (alpha >= beta) {
                        return alpha;
                    }

                    //-------------------------------------------------
                }
                else {
                    return Integer.MAX_VALUE;
                }
            }
        }
        return nova_alpha;
    }
    
     /**
     * Funcion que ens indica l'heuristica mes petita trobada per totes les tirades analitzades.
     * @param t taulell sobre el que s'esta jugant
     * @param profunditat profunditat fins la que s'explorara l'arbre de posibilitats
     * @param alpha valor heuristic mes alt trobat fins al moment per fer la poda
     * @param beta valor heuristic mes baix trobat fins al moment per fer la poda
     * @return retorna la heuristica mes baixa de totes les tirades analitzades
     */
    //Descomentar si es vol sense poda
    //public int minimitza (Tauler t,int profunditat){
    //Comentar si es vol sense poda
    public int minimitza (Tauler t,int profunditat,int alpha,int beta){
        if (profunditat <= 0 || !(t.espotmoure())) {
            return heur(t);
        }
        int nova_beta = Integer.MAX_VALUE;
        for (int i = 0; i < t.getMida(); i++) {
            if (t.movpossible(i)) {
                Tauler taulell_aux = new Tauler(t);
                taulell_aux.afegeix(i, this.color*-1);
                if (!taulell_aux.solucio(i, this.color*-1)) {
                    //Descomentar si es vol sense poda
                    //nova_beta = Math.min(nova_beta, maximitza(taulell_aux, profunditat - 1));
                    
                    //---Comentar si es vol sense poda-----------------
                    
                    nova_beta = Math.min(nova_beta, maximitza(taulell_aux, profunditat - 1, alpha, beta));
                    beta = Math.min(nova_beta, beta);
                    if (alpha >= beta) {
                        return beta;
                    }

                    //-------------------------------------------------
                }
                else {
                    return Integer.MIN_VALUE;
                }
            }
        }
        return nova_beta;
    }
    
    /**
     * Funcion que  indica la puntuacion d'una casella segons el color
     * @param t taulell sobre el que estem jugant
     * @param i fila 
     * @param j columna
     * @param color color de qui esta comprobant les caselles per puntarse
     * @return retorna 3 si la casella es del mateix color i -1 si la casella es de l'altre color 
     *         (sempre que la profunditat sigui > 4)
     */
    public int puntua (Tauler t, int i, int j, int color) {
        int puntuacio = 0;
        if (t.getColor(i, j) != 0) {
            if (t.getColor(i, j) == color) {
                //if (this.prof > 4) //proba
                    puntuacio = 3;                
            }
            else {
                //if (this.prof > 4) //proba
                    puntuacio = -1;
            }
        }
        return puntuacio;
    }
    
    /**
     * Funcio que calcula l'heuristica del taulell combinant l'heuristica del taulell
     * de probabilitats i explorant les caselles veines de cada ficha
     * Sumem heuristica si son nostres les fiches i restem si son del contrincant
     * @param t taulell sobre el que estem jugant
     * @return retorna la restea del a nostre heuristica menys la del contrincant
     */
    public int heur(Tauler t) {
        numNodes = numNodes + 1;
        int puntuacio_meva = 0;
        int puntuacio_enemic = 0;
        int size = t.getMida();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (t.getColor(i, j) == this.color) {
                    puntuacio_meva += taula_posibilitat[i][j];
                } else if (t.getColor(i, j) == this.color*-1) {
                    puntuacio_enemic += taula_posibilitat[i][j];
                }
                if (t.getColor(i, j) == this.color) {
                    int hor = puntuacio_horitzontal(t,i,j,this.color);
                    int ver = puntuacio_vertical(t,i,j,this.color);
                    int diag = puntuacio_diagonal(t,i,j,this.color);
                    puntuacio_meva += hor+ver+diag;
                }
                else if (t.getColor(i, j) == this.color*-1) {
                    int hor = puntuacio_horitzontal(t,i,j,this.color*-1);
                    int ver = puntuacio_vertical(t,i,j,this.color*-1);
                    int diag = puntuacio_diagonal(t,i,j,this.color*-1);
                    puntuacio_enemic += hor+ver+diag;  
                }
            }
        }
        this.result_heur = puntuacio_meva - puntuacio_enemic;
        return puntuacio_meva - puntuacio_enemic;
    }
    
    /**
     * Funcion encarregada de visitar les caselles veines en horitzontal
     * i donar una puntuacio segons el color de cada casella.
     * @param t taulell sobre el que juguem
     * @param i fila de la casella que puntuem 
     * @param j columna de la casella que puntuem
     * @param color color del jugador que esta puntuan el taulell
     * @return Retorna la puntuacio de la casella segons el color de les caselles veines en horitzontal
     */
    public int puntuacio_horitzontal(Tauler t, int i, int j, int color){
        int puntuacio = 0;
        int size = t.getMida();
        for (int x = 0; x < 4; x++) {
            if (i-x < 0 || i+x >= size || j-x < 0 || j+x >= size) {
                break;
            }
            int esq = puntua(t,i-x,j,color);
            int dre = puntua(t,i+x,j,color);
            puntuacio += esq + dre;
        }
        return puntuacio;
    }
    
     /**
     * Funcion encarregada de visitar les caselles veines en vertical
     * i donar una puntuacio segons el color de cada casella.
     * @param t taulell sobre el que juguem
     * @param i fila de la casella que puntuem 
     * @param j columna de la casella que puntuem
     * @param color color del jugador que esta puntuan el taulell
     * @return Retorna la puntuacio de la casella segons el color de les caselles veines en vertical
     */
    public int puntuacio_vertical(Tauler t, int i, int j, int color){
        int puntuacio = 0;
        int size = t.getMida();
        for (int x = 0; x < 4; x++) {
            if (i-x < 0 || i+x >= size || j-x < 0 || j+x >= size) {
                break;
            }
            int punt = puntua(t,i,j+x,color);
            puntuacio += punt;
        }
        return puntuacio;
    }
    
     /**
     * Funcion encarregada de visitar les caselles veines en a les diagonals superiors
     * i donar una puntuacio segons el color de cada casella.
     * @param t taulell sobre el que juguem
     * @param i fila de la casella que puntuem 
     * @param j columna de la casella que puntuem
     * @param color color del jugador que esta puntuan el taulell
     * @return Retorna la puntuacio de la casella segons el color de les caselles veines en les diagonals superiors
     */
    public int puntuacio_diagonal(Tauler t, int i, int j, int color){
        int puntuacio = 0;
        int size = t.getMida();
        for (int x = 0; x < 4; x++) {
            if (i-x < 0 || i+x >= size || j-x < 0 || j+x >= size) {
                break;
            }
            int esq = puntua(t,i-x,j+x,color);
            int dre = puntua(t,i+x,j+x,color);
            puntuacio += esq + dre;
        }
        return puntuacio;
    }
}
