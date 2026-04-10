package fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonSeurantaRepository implements SeurantaRepository {

    private final Path kategorioidenTallennusTiedosto;
    private final Path tapahtumienTallenusTiedosto;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonSeurantaRepository(Path kategorioidenTallennusTiedosto, Path tapahtumienTallenusTiedosto) {
        this.kategorioidenTallennusTiedosto = kategorioidenTallennusTiedosto;
        this.tapahtumienTallenusTiedosto = tapahtumienTallenusTiedosto;
    }

    @Override
    public List<Kategoria> lataaKategoriat() throws JacksonException {
        if (Files.notExists(kategorioidenTallennusTiedosto)) {
            return List.of();
        }
        return mapper.readValue(kategorioidenTallennusTiedosto.toFile(), new TypeReference<>() {
        });
    }

    @Override
    public void tallennaKategoriat(List<Kategoria> kategoriat) throws JacksonException {
        mapper.writeValue(kategorioidenTallennusTiedosto.toFile(), kategoriat);
    }

    public List<Tapahtuma> lataaTapahtumat() throws JacksonException {
        if (Files.notExists(tapahtumienTallenusTiedosto)) {
            return List.of();
        }
        return mapper.readValue(tapahtumienTallenusTiedosto.toFile(), new TypeReference<>() {
        });
    }

    @Override
    public void tallennaTapahtumat(List<Tapahtuma> tapahtumat) throws JacksonException {
        mapper.writeValue(tapahtumienTallenusTiedosto.toFile(), tapahtumat);
    }
}

