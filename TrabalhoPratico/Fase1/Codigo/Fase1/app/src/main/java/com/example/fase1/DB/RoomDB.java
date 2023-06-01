package com.example.fase1.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fase1.Converters.Converters;
import com.example.fase1.DAO.AppDAO;
import com.example.fase1.DAO.ContactDAO;
import com.example.fase1.DAO.EdgeDAO;
import com.example.fase1.DAO.InfoPointDAO;
import com.example.fase1.DAO.InfoTrailDAO;
import com.example.fase1.DAO.MediaDAO;
import com.example.fase1.DAO.PointHistoryDAO;
import com.example.fase1.DAO.PointOfInterestDAO;
import com.example.fase1.DAO.TrailDAO;
import com.example.fase1.DAO.TrailFavoriteDAO;
import com.example.fase1.DAO.TrailHistoryDAO;
import com.example.fase1.DAO.UserDAO;
import com.example.fase1.Data.App;
import com.example.fase1.Data.Contact;
import com.example.fase1.Data.Edge;
import com.example.fase1.Data.InfoPoint;
import com.example.fase1.Data.InfoTrail;
import com.example.fase1.Data.Media;
import com.example.fase1.Data.Partner;
import com.example.fase1.Data.PointHistory;
import com.example.fase1.Data.PointOfInterest;
import com.example.fase1.Data.Social;
import com.example.fase1.Data.Trail;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.Data.TrailHistory;
import com.example.fase1.Data.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities =
                {Contact.class,
                Edge.class,
                Media.class,
                PointOfInterest.class,
                Trail.class,
                TrailHistory.class,
                PointHistory.class,
                InfoTrail.class,
                InfoPoint.class,
                Partner.class,
                Social.class,
                App.class,
                User.class,
                TrailFavorite.class
                }, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {

    public abstract ContactDAO contactDAO();
    public abstract EdgeDAO edgeDAO();
    public abstract MediaDAO mediaDAO();
    public abstract PointOfInterestDAO pointOfInterestDAO();
    public abstract TrailDAO trailDAO();
    public abstract TrailHistoryDAO trailHistoryDAO();
    public abstract PointHistoryDAO pointHistoryDAO();
    public abstract InfoTrailDAO infoTrailDAO();
    public abstract InfoPointDAO infoPointDAO();
    public abstract AppDAO appDAO();
    public abstract UserDAO userDAO();
    public abstract TrailFavoriteDAO trailFavoriteDAO();

    private static volatile RoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, "database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

