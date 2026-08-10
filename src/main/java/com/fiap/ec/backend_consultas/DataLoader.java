package com.fiap.ec.backend_consultas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.repository.ConsultaRepository;
import com.fiap.ec.backend_consultas.repository.EspecialidadeRepository;
import com.fiap.ec.backend_consultas.repository.MedicoRepository;
import com.fiap.ec.backend_consultas.repository.PacienteRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public DataLoader(ConsultaRepository consultaRepository,
                      MedicoRepository medicoRepository,
                      PacienteRepository pacienteRepository,
                      EspecialidadeRepository especialidadeRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.especialidadeRepository = especialidadeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (consultaRepository.count() > 0) {
            System.out.println("DataLoader: consultas já existem, pulando seed.");
            return;
        }

        Especialidade especialidade = especialidadeRepository.findAll().stream().findFirst().orElseGet(() ->
                especialidadeRepository.save(new Especialidade("Cardiologia"))
        );

        List<Medico> medicos = medicoRepository.findAll();
        if (medicos.isEmpty()) {
            medicos = List.of(
                    medicoRepository.save(new Medico("Dr. Roberto Silva", "123456/SP", especialidade)),
                    medicoRepository.save(new Medico("Dra. Ana Costa", "654321/SP", especialidade))
            );
        }

        List<Paciente> pacientes = pacienteRepository.findAll();
        if (pacientes.isEmpty()) {
            pacientes = List.of(
                    pacienteRepository.save(new Paciente("João Silva", "123.456.789-00", "joao@email.com", "11999999999", LocalDate.of(1990, 1, 1), true)),
                    pacienteRepository.save(new Paciente("Maria Souza", "987.654.321-11", "maria@email.com", "11888888888", LocalDate.of(1985, 5, 12), true))
            );
        }

        Medico medico1 = medicos.get(0);
        Medico medico2 = medicos.size() > 1 ? medicos.get(1) : medico1;
        Paciente paciente1 = pacientes.get(0);
        Paciente paciente2 = pacientes.size() > 1 ? pacientes.get(1) : paciente1;

        consultaRepository.saveAll(List.of(
                new Consulta(medico1, paciente1,
                        LocalDateTime.of(2026, 5, 20, 9, 0), "agendada", 250.00,
                        "Consulta de rotina"),
                new Consulta(medico2, paciente2,
                        LocalDateTime.of(2026, 5, 21, 14, 30), "confirmada", 350.00,
                        "Retorno pós-exame"),
                new Consulta(medico1, paciente2,
                        LocalDateTime.of(2026, 5, 15, 10, 0), "realizada", 200.00,
                        null),
                new Consulta(medico2, paciente1,
                        LocalDateTime.of(2026, 5, 18, 11, 0), "cancelada", 300.00,
                        "Paciente desmarcou")
        ));

        System.out.println("DataLoader: Dados de médicos, pacientes e 4 consultas criados com sucesso!");
    }
}