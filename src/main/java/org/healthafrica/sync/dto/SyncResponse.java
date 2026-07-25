package org.healthafrica.sync.dto;
import java.util.List;


public record SyncResponse(

        List<SyncResult> results

) {
}