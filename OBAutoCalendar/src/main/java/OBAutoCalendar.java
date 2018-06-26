import java.util.*;

public class OBAutoCalendar {
   public static void main(String[] args) 
   { 
      String email_id = args[0];
      String password = args[1];
      List<Correo> correosGDCArg = DescargaCorreos.getCorreos(email_id, password, ArgentinaGDC.getNombreCarpetaInbox(), ArgentinaGDC.getNombreCarpetaLeidos());
      List<Correo> correosGDIArg = DescargaCorreos.getCorreos(email_id, password, ArgentinaGDI.getNombreCarpetaInbox(), ArgentinaGDI.getNombreCarpetaLeidos());
      List<CPUEvent> lArgGDCEvents = new ArrayList<CPUEvent>();
      List<CPUEvent> lArgGDIEvents = new ArrayList<CPUEvent>();

      try{
         if(correosGDCArg.size()>0) {
            for (Correo corr : correosGDCArg) {
               ArgentinaGDC evArgGDC = new ArgentinaGDC(corr);
               lArgGDCEvents.add(evArgGDC);
            }
            CalendarInteract.subirEvents(lArgGDCEvents, ArgentinaGDC.getCalendarioAsignado1());

         }
         if(correosGDIArg.size()>0) {
            for (Correo corr : correosGDIArg) {
               ArgentinaGDI evArgGDI = new ArgentinaGDI(corr);
               lArgGDIEvents.add(evArgGDI);
            }
            CalendarInteract.subirEvents(lArgGDIEvents, ArgentinaGDI.getCalendarioAsignado1());

         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      try {
         CalendarInteract.borrarDuplicados(ArgentinaGDI.getCalendarioAsignado1());
         CalendarInteract.borrarDuplicados(ArgentinaGDI.getCalendarioAsignado2());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
