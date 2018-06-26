import com.google.api.services.calendar.model.*;
import com.google.api.client.util.DateTime;

import java.text.*;


public class ArgentinaGDI extends CPUEvent
{
// This class represents the way we will process mails from GDC Argentina and convert them into Calendar.Event objects

    public static String asuntoEv;
    private Event event;
    public static String carpetaInbox = "GDI_Notificaciones";
    public static String carpetaLeidos = "INBOX/provisional-GDI";
    public static String calendarioAsignado1 = "Ventanas Arg";
    public static String calendarioAsignado2 = "Ventanas GDI/GDC";
    private String numNotificacion;
    private DateTime fechaRecibido;
    // var tipoNotif is for record the type of the notification, and can be:
    //   1 -> Normal;  2 -> Update; 3 -> Cancel; 4 -> Fin
    private int tipoNotif = 0;

    // var notifNum is for separate the number of the notification
    private String notifNum = "";

    // var tipoNotificacion is for having in a String the kind of notification we have ("Inicio", "Fin", "Fin (Cancelada)", etc)
    private String tipoNotificacion = "";

    public ArgentinaGDI(Event even)
    {
        setEvent(new Event());
        setRemitenteEsperado("GDI-Notificaciones@tgtarg.com");
        this.tipo = "GDI";
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

    public ArgentinaGDI(Correo corr)
    {
        setEvent(new Event());
        //setCarpeta("GDI_Notificaciones");
        setRemitenteEsperado("GDI-Notificaciones@tgtarg.com");
        this.tipo = "GDI";
        this.setFechaRecibido(corr.fechaRecepcion);
        // We set the summary of the Calendar Event object to the subject of the mail
        getEvent().setSummary(this.tipo + " " + corr.getAsunto());
        // We call the funtion textoEvento to get the text between the string "Notificacion Nro " and the first space
        if(getEvent().getSummary()!=null)
        {
            notifNum = textoEvento(getEvent().getSummary(), "Nro ", ' ');
            tipoNotificacion = textoEvento(getEvent().getSummary(), getNotifNum() + " - ", '-');
            tipoNotif = setTipoNotif();
            // We set the location of the Calendar Event object to the concatenation of the GDC ticket ID we can see in the mail body after the text: "Evento Lista de Mensajes: ",
            // and the received date value as a String
            getEvent().setLocation(notifNum);

            numNotificacion = getEvent().getSummary();
        }
        if(corr.getCuerpo()!=null)
        {
            String auxiliar = "";
            auxiliar = "Módulo: " + textoEvento(corr.getCuerpo(), "Modulo: ");
            auxiliar = auxiliar + "\nEvento Lista de Mensajes: " + textoEvento(corr.getCuerpo(), "Evento Lista de Mensajes: ");
            auxiliar = auxiliar + "\nDescripción: " + textoEvento(corr.getCuerpo(), "Descripcion: ");
            auxiliar = auxiliar + "\nCausa: " + textoEvento(corr.getCuerpo(), "Causa: ");
            auxiliar = auxiliar + "\nFecha de recepcion: " + fechaRecibido.toString() + "\n";
            getEvent().setDescription(auxiliar);


            // Implementar la fecha y hora del event
            String fechaIni = textoEvento(corr.getCuerpo(), "Fecha y Hora de Inicio: ");
            String fechaFin = "";
            auxiliar = textoEvento(corr.getCuerpo(), "Fecha y Hora de Fin: ");
            if(auxiliar == "")
                auxiliar = (textoEvento(corr.getCuerpo(), "Fecha y Hora Estimada de Solucion: "));
            fechaFin = auxiliar;
            getEvent().setStart(fechaFromString(fechaIni));
            getEvent().setEnd(fechaFromString(fechaFin));

        }
    }

    public void setFechaRecibido(DateTime fecha) {
        this.fechaRecibido = fecha;
    }

    public String getNotifNum() { return this.notifNum;
    }


    // The method below returns the EventDateTime with the value of the date String received as parameter formatted as is propper to this standard mail instance
    public EventDateTime fechaFromString(String fecha)
    {
        String fechaCompleta = "";
        DateTime date;
        Long ey;
        if(fecha.length() > 16)
        {
            String cadInputDia = fecha.substring(0, 10);
            String cadInputHora = fecha.substring(11, fecha.indexOf('h', 11));
            String cadOutputDia = "";
            String cadOutputHora = "";

            SimpleDateFormat formatoAntDia = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoNuevoDia = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat formatoAntHora = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatoNuevoHora = new SimpleDateFormat("HH:mm:ss.SSS");

            try {
                cadOutputDia = formatoNuevoDia.format(formatoAntDia.parse(cadInputDia));
                cadOutputHora = formatoNuevoHora.format(formatoAntHora.parse(cadInputHora));
                fechaCompleta = cadOutputDia + "T" + cadOutputHora;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
            System.out.println("Cadena de fecha no válida: \n" + fecha);

        date = new DateTime(fechaCompleta);
        ey = date.getValue() + 10800000L;
        // Igualo un Long al instante mas 3 horas para poder establecer el -180 en el constructor del DateTime
        EventDateTime devolver = new EventDateTime()
                .setDateTime(new DateTime(ey, -180))
                .setTimeZone("America/Argentina/Buenos_Aires");
        return devolver;
    }

    public  Event getEvent()
    {
        return event;
    }

    public  void setEvent(Event ev)
    {
        event = ev;
    }
    public String getTipoNotificacion()
    {
        return tipoNotificacion;
    }

    public String getAsuntoEv()
    {
        return asuntoEv;
    }

    public void setAsuntoEv(String as)
    {
        asuntoEv = as;
    }

    public DateTime getFechaRecibido() {
        return fechaRecibido;
    }


    public static String getCalendarioAsignado1()
    {
        return calendarioAsignado1;
    }
    public static String getCalendarioAsignado2()
    {
        return calendarioAsignado2;
    }


    public int getTipoNotif()
    {
        return tipoNotif;
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

    public static String getNombreCarpetaInbox()
    {
        return carpetaInbox;
    }
    public static String getNombreCarpetaLeidos()
    {
        return carpetaLeidos;
    }
}
