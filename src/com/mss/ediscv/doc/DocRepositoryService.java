/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.doc;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface DocRepositoryService {

    public ArrayList<DocRepositoryBean> buildDocumentQuery(DocRepositoryAction docbean) throws ServiceLocatorException;
}
