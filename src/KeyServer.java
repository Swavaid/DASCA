import Jama.Matrix;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.poi.ss.formula.EvaluationWorkbook;

import java.math.BigInteger;

/**
 * @author emilio
 * @date 2022-12-02 10:28
 */
public class KeyServer {

    //setup sub-matrix B_i(x,y)


    //B(x,y)
    public Element[][] a_Matrix;

    //three public factor
    public Element P_0;


    public Element[] P_x;
    public Element[] P_y;

    //set up B_i(j,y) full share's sub polynomial
    public Element[][] coefficient_x;


    //set up B_i(x,j) reduced share's sub polynomial
    public Element[][] coefficient_y;

    //receive others' B_j(x,i)
    public Element [][][] received_poly_x;


    public Element [][] coefficient_x_arr;


    //receive others' B_j(i,y)
    public Element [][][] received_poly_y;

    public Element [][] coefficient_y_arr;



    //B(i,y)
    public Element [] full_share;
    //B(x,i)
    public Element [] reduced_share;

    //secret share B(i,0)
    public Element secret_share;
    //public share B(i,0)P
    public Element public_share;

    public Element csk;
    public Element[] cpk;

    public Element[] commit_reducedShares;



    public Element received_blind_keywords;

    public Element[][] witness;

    public Element[] witness_for_update;
    public int[] reduced_share_update_x;
    public Element[] reduced_share_update_y;

    public Element[] reduced_Share_reconstruct;

    public Element[] sub_P_x_for_update;
    public Element[] received_P_x_for_update;
    public Element[] R_x_for_update;
    public Element s_m;
    public Element s_m_P;
    public Element[] Z_m_x;

    public Element C_Z_m_x;
    public Element C_new_B_x_m;

    public Element W_z_m_0;

    public Element[] new_reduced_share;

    public Element[] final_sub_reduced_share;
    public int[] final_x;
    public Element[] final_witness_reduced_share;
    public Element[] new_full_share;




}
