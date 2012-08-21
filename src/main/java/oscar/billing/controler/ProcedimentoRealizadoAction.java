
package oscar.billing.controler;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.billing.cad.dao.CadProcedimentoDAO;
import oscar.billing.cad.dao.CidDAO;
import oscar.billing.cad.dao.TipoAtendimentoDAO;
import oscar.billing.cad.model.CadCid;
import oscar.billing.cad.model.CadProcedimentos;
import oscar.billing.dao.AppointmentDAO;
import oscar.billing.dao.DiagnosticoDAO;
import oscar.billing.dao.ProcedimentoRealizadoDAO;
import oscar.billing.fat.dao.FatFormulariosDAO;
import oscar.billing.model.ProcedimentoRealizado;
import oscar.util.OscarAction;

@Deprecated
public class ProcedimentoRealizadoAction extends OscarAction {
    private static Logger logger = MiscUtils.getLogger();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        ProcedimentoRealizadoForm procedimentoRealizadoForm = (ProcedimentoRealizadoForm) form;
        String myaction = mapping.getParameter();
        logger.debug(" [ProcedimentoRealizadoAction] My action = " + myaction);

        if (isCancelled(request)) {
            logger.info(" [ProcedimentoRealizadoAction] " +
                mapping.getAttribute() + " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("ADD".equalsIgnoreCase(myaction)) {
            String dispatch = procedimentoRealizadoForm.getDispatch();

            if ("formulario".equals(dispatch)) {
                myforward = performUpdateFormulario(mapping, form, request,
                        response);
            }

            if ("procedimento".equals(dispatch)) {
                myforward = performUpdateProcedimento(mapping, form, request,
                        response);
            }
            
			if ("add_proc".equals(dispatch)) {
				myforward = performAddProcedimento(mapping, form, request,
						response);
			}

			if ("diagnostico".equals(dispatch)) {
				myforward = performUpdateDiagnostico(mapping, form, request,
						response);
			}            

            if ("gravar".equals(dispatch)) {
                myforward = performGravar(mapping, form, request, response);
            }
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else if ("DEL_PROC".equalsIgnoreCase(myaction)) {
            myforward = performDeleteProcedimento(mapping, form, request,
                    response);
        } else if ("DEL_CID".equalsIgnoreCase(myaction)) {
		myforward = performDeleteDiagnostico(mapping, form, request,
				response);
	} else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug(" [ProcedimentoRealizadoAction] INIT");

        ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;
        HttpSession session = request.getSession();

        try {
            String appId = request.getParameter("appId");

            if (appId == null || appId.trim().length() <= 0) {
                generalError(request, "faturamento.notfound");

                return mapping.findForward("failure");
            }

            form.clear();
            logger.debug(" [ProcedimentoRealizadoAction] Limpou form");

            FatFormulariosDAO formDAO = new FatFormulariosDAO();
            AppointmentDAO appDAO = new AppointmentDAO();
            ProcedimentoRealizadoDAO prDAO = new ProcedimentoRealizadoDAO();
            DiagnosticoDAO diagDAO = new DiagnosticoDAO();
            TipoAtendimentoDAO tpAtendDAO = new TipoAtendimentoDAO();

            //Obter appointment
            form.setAppointment(appDAO.retrieve(appId));

            //obter lista de formularios
            form.setFormularios(formDAO.list());
            session.setAttribute("FORMULARIOS", form.getFormularios());
            logger.info("size = " + form.getFormularios().size());
            
            //Obter procedimentos realizados
            form.getAppointment().setProcedimentoRealizado(prDAO.list(appId));

            //Obter diagnosticos realizados
            form.getAppointment().setDiagnostico(diagDAO.list(appId));
            logger.info(
                " [ProcedimentoRealizadoAction] setou procedimentos e diagnosticos no form");
                
            // get attendance types
            session.setAttribute("TPATENDIMENTO", tpAtendDAO.list());
            List procs = form.getAppointment().getProcedimentoRealizado();
            if (procs.size() > 0) {
            	// types should be all the same, so take the first one
            	ProcedimentoRealizado proc = (ProcedimentoRealizado) procs.get(0);
	            form.setTpAtendimento(
	            	Long.toString(proc.getTpAtendimento().getCoTipoatendimento()));
            }
			
        } catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performUpdateFormulario(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ProcedimentoRealizadoAction] UPDATE_FORMULARIO");

        ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;

        try {
            FatFormulariosDAO formDAO = new FatFormulariosDAO();

            //selecionar procedimentos do formulario XXX
            Vector v = new Vector(formDAO.listProcedimentoByForm(String.valueOf(
                            form.getFormulario().getCoFormulario())));
            logger.info("vector " + v.size());
            form.setProcedimentosForm(v);
            
            form.setFormulario(formDAO.retrieve(String.valueOf(form.getFormulario().getCoFormulario())));
            
        } catch (Exception e) {
            generalError(request, e, "error.general");

            return mapping.findForward("failure");
        }

        return mapping.findForward("formulario");
    }

    private ActionForward performUpdateProcedimento(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ProcedimentoRealizadoAction] UPDATE_PROCEDIMENTO");

        ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;

        try {
            FatFormulariosDAO formDAO = new FatFormulariosDAO();

            //inserir procedimentos selecionados nos realizados
            form.getAppointment().addProcedimentos(formDAO.listProcedimentoByProc(
                    form.getProcedimentosChecked()));
                    
            form.setProcedimentosChecked(new String[] {});
        } catch (Exception e) {
            generalError(request, e, "error.general");

            return mapping.findForward("failure");
        }

        return mapping.findForward("procedimento");
    }

    private ActionForward performDeleteProcedimento(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ProcedimentoRealizadoAction] DELETE_PROCEDIMENTO");

        ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;

        try {
            long id = Long.parseLong(request.getParameter("coProc"));
            logger.info("id " + id );
            
            //apagar procedimentos realizados
            form.getAppointment().removeProcedimentos(id);
        } catch (Exception e) {
            generalError(request, e, "error.general");

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

	private ActionForward performUpdateDiagnostico(ActionMapping mapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) {
		logger.info(" [ProcedimentoRealizadoAction] UPDATE_DIAGNOSTICO");

		ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;

		try {
			CidDAO diagDAO = new CidDAO();
						
			//inserir diagnostico
			CadCid diag = diagDAO.retrieve(form.getCoCid().trim());
			if (diag.getCoCid() != null && !diag.getCoCid().equals("")) { 
			   form.getAppointment().addDiagnostico(diag);
			}
                    
			form.setCoCid("");
			form.setDsCid("");
		} catch (Exception e) {
			generalError(request, e, "error.general");

			return mapping.findForward("failure");
		}

		return mapping.findForward("diagnostico");
	}

	private ActionForward performDeleteDiagnostico(ActionMapping mapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) {
		logger.info(" [ProcedimentoRealizadoAction] DELETE_DIAGNOSTICO");

		ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;

		try {
			String id = request.getParameter("coCid");
			logger.info("id " + id );
            
			//apagar diagnostico realizados
			form.getAppointment().removeDiagnostico(id);
			form.setCoCid("");
		} catch (Exception e) {
			generalError(request, e, "error.general");

			return mapping.findForward("failure");
		}

		return mapping.findForward("success");
	}

    private ActionForward performGravar(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ProcedimentoRealizadoAction] GRAVAR");

        ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;
        try {
			AppointmentDAO appDAO = new AppointmentDAO();
			ProcedimentoRealizadoDAO procRDAO = new ProcedimentoRealizadoDAO();
			
            // gravar procedimentos realizados e diagnosticos - save
            appDAO.billing(form.getAppointment());
            
            // after saving, updates the attendance types
            procRDAO.updateAttendanceType(
            	Long.toString(form.getAppointment().getAppointmentNo()), 
            	form.getTpAtendimento());
            
        } catch (Exception e) {
            generalError(request, e, "error.general");

            return new ActionForward(mapping.getInput());
        }

        return mapping.findForward("gravar");
    }

	private ActionForward performAddProcedimento(ActionMapping mapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) {
		logger.info(" [ProcedimentoRealizadoAction] ADD_PROC");

		ProcedimentoRealizadoForm form = (ProcedimentoRealizadoForm) actionForm;

		try {
			CadProcedimentoDAO procDAO = new CadProcedimentoDAO();
						
			//inserir procedimento
			CadProcedimentos proc = procDAO.retrieve(String.valueOf(form.getCadProcedimentos().getCoProcedimento()));
			if (proc.getCoProcedimento() != 0) { 
			   form.getAppointment().addProcedimentos(proc);
			}
                    
			form.getCadProcedimentos().setCoProcedimento(0);
			form.getCadProcedimentos().setDsProcedimento("");
		} catch (Exception e) {
			generalError(request, e, "error.general");

			return mapping.findForward("failure");
		}

		return mapping.findForward("procedimento");
	}

}
