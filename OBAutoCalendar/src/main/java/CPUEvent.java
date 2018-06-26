import com.google.api.services.calendar.model.*;
import com.google.api.client.util.DateTime;
import java.text.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class CPUEvent
{
   public static String remitenteEsperado;
   public static String calendarioAsignado1;
   public Event event;
   public String tipo;
   public CPUEvent() {
   }
   public Event getEvent()
   {
      return this.event;
   }
   public DateTime getFechaRecibido() {
      return fechaRecibido;
   }
   public static String getRemitenteEsperado()
   {
      return remitenteEsperado;
   }

   public static String getCalendarioAsignado1()
   {
      return calendarioAsignado1;
   }

   public static void setCalendarioAsignado(String cal)
   {
      calendarioAsignado1 = cal;
   }

   public static void setRemitenteEsperado(String rem)
   {
      remitenteEsperado = rem;
   }

   public String numNotificacion;
   public DateTime fechaRecibido;
   // var tipoNotif is for record the type of the notification, and can be:
   //   1 -> Normal;  2 -> Update; 3 -> Cancel; 4 -> Fin
   public int tipoNotif = 0;

   // var notifNum is for separate the number of the notification
   public String notifNum = "";

   // var tipoNotificacion is for having in a String the kind of notification we have ("Inicio", "Fin", "Fin (Cancelada)", etc)
   public String tipoNotificacion = "";


   public EventDateTime fechaFromString(String fecha)
   {EventDateTime ev = null;
   return ev;}

   public CPUEvent(Event even){
      setEvent(new Event());
      this.tipo = "GDC";
      setRemitenteEsperado("GDC-Notificaciones@tgtarg.com");
      getEvent().setSummary(even.getSummary());
      notifNum = even.getLocation();
      tipoNotificacion = textoEvento(getEvent().getSummary(), getNotifNum() + " - ", '-');
      tipoNotif = setTipoNotif();
      getEvent().setLocation(notifNum);
      getEvent().setDescription(even.getDescription());
      getEvent().setStart(even.getStart());
      getEvent().setEnd(even.getEnd());
      numNotificacion = getEvent().getSummary();
      fechaRecibido = new DateTime(textoEvento(even.getDescription(), "Fecha de recepcion: "));
   }

   public CPUEvent(Correo corr){

   }

   public  void setEvent(Event ev)
   {
      event = ev;
   }
   public void setFechaRecibido(DateTime fecha) {
      this.fechaRecibido = fecha;
   }

   public String getNotifNum() { return this.notifNum;
   }

   public String textoEvento(String mail, String tag, char hasta)
   {
      int indiceTag = 0;
      int indiceSalto = 0;
      int inicio = 0;
      int fin = 0;
      char[] cMail;
      String cadena = "";
      String cadenaChars = "";
      indiceTag = mail.indexOf(tag);
      inicio = indiceTag + tag.length();
      indiceSalto = mail.indexOf(hasta,inicio);
      if(hasta == ' ')
      {
         fin = indiceSalto;
      }
      else
         fin = indiceSalto - 1;
      
      if(indiceTag > 0)
      {
         cMail = new char[mail.length()];
         mail.getChars(inicio, fin, cMail, 0);
         cadena = new String (cMail);
         cadenaChars = new String (cMail);
         cadena = cadenaChars.substring(0, cadenaChars.indexOf('\u0000'));
      }
      return cadena;
    }

   public int setTipoNotif()
   {
      if(tipoNotificacion.contains("Actualizacion") || tipoNotificacion.contains("Fe de Erratas"))
         return 2;
      else if(tipoNotificacion.contains("Cancelada"))
         return 3;
      else if(tipoNotificacion.contains("Fin"))
         return 4;
      else
         return 1;
   }

   public String textoEvento(String mail, String tag)
   {
      int indiceTag = 0;
      int indiceSalto = 0;
      int inicio = 0;
      int fin = 0;
      char[] cMail;
      String cadena = "";
      String cadenaChars = "";
      indiceTag = mail.indexOf(tag);
      indiceSalto = mail.indexOf('\n',indiceTag);
      inicio = indiceTag + tag.length();
      fin = indiceSalto - 1;
      if(indiceTag > 0)
      {
         cMail = new char[mail.length()];
         mail.getChars(inicio, fin, cMail, 0);
         cadena = new String (cMail);  
         indiceTag = mail.indexOf(tag);
         indiceSalto = mail.indexOf('\n',indiceTag);
         mail.getChars(indiceTag+tag.length(), indiceSalto, cMail, 0);
         cadenaChars = new String (cMail);
         cadena = cadenaChars.substring(0, cadenaChars.indexOf('\u0000'));
      }
      return cadena;
    }
}