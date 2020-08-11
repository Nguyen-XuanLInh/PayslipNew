package com.wata.payslip.service.Implements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wata.payslip.converter.ProjectConverter;
import com.wata.payslip.model.dto.ProjectDTO;
import com.wata.payslip.model.dto.SearchData;
import com.wata.payslip.model.entity.ProjectEntity;
import com.wata.payslip.model.entity.TypeProjectEntity;
import com.wata.payslip.repository.ProjectRepository;
import com.wata.payslip.repository.TypeProjectRepository;
import com.wata.payslip.service.Interface.IProjectService;

@Service
public class ProjectService implements IProjectService {
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	TypeProjectRepository typeProjectRepository;

	@Autowired
	private ProjectConverter projectConverter;

	@Autowired
	private EntityManager entityManager;

	@Override
	public ResponseEntity<Map<String, Object>> createProject(ProjectDTO projectDTO) {

		ProjectEntity entity = projectConverter.toEntity(projectDTO);
		try {
			TypeProjectEntity typeProjectEntity = entityManager.getReference(TypeProjectEntity.class,
					projectDTO.getIdTypeProject());
			entity.setTypeProject(typeProjectEntity);
			entity = projectRepository.save(entity);
			Map<String, Object> response = new HashMap<>();
			response.put("Project", projectConverter.toDTO(entity));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getAllProject() {
		try {
			List<ProjectDTO> dtoes = projectConverter.toDTOs(projectRepository.findAll());
			Map<String, Object> response = new HashMap<>();
			response.put("Projects", dtoes);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getProjectById(Integer idProject) {
		try {
			ProjectEntity entity = projectRepository.findOneByIdProject(idProject);
			Map<String, Object> response = new HashMap<>();
			response.put("Projects", projectConverter.toDTO(entity));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> deleteProjectById(Integer idProject) {
		try {
			projectRepository.deleteById(idProject);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> updateProjectById(ProjectDTO projectDTO, Integer idProject) {
		try {
			ProjectEntity entity = projectRepository.findOneByIdProject(idProject);
			if (projectDTO.getIdTypeProject() != null) {
				TypeProjectEntity typeProjectEntity = entityManager.getReference(TypeProjectEntity.class,
						projectDTO.getIdTypeProject());
				entity.setTypeProject(typeProjectEntity);
			}

			if (projectDTO.getNameProject() != null) {
				entity.setNameProject(projectDTO.getNameProject());
			}

			if (projectDTO.getStartDate() != null) {
				entity.setStartDate(projectDTO.getStartDate());
			}

			if (projectDTO.getEndDate() != null) {
				entity.setEndDate(projectDTO.getEndDate());
			}

			if (projectDTO.getDescription() != null) {
				entity.setDescription(projectDTO.getDescription());
			}

			Map<String, Object> response = new HashMap<>();
			response.put("Project", projectConverter.toDTO(projectRepository.save(entity)));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> searchByNameProject(SearchData searchData) {
		String nameProject = searchData.getSearchValue();
		Integer currentPage, pageSize;
		String sort = searchData.getSort();

		if (searchData.getCurrentPage() != null) {
			currentPage = searchData.getCurrentPage();
		} else {
			currentPage = 0;
		}

		if (searchData.getPageSize() != null) {
			pageSize = searchData.getPageSize();
		} else {
			pageSize = 3;
		}

		try {
			List<ProjectEntity> projectEntities = new ArrayList<ProjectEntity>();
			Pageable paging;

			if (sort != null) {
				switch (sort) {
				case "ASC":
					paging = PageRequest.of(currentPage, pageSize, Sort.by("nameProject"));
					break;
				case "DESC":
					paging = PageRequest.of(currentPage, pageSize, Sort.by("nameProject").descending());
					break;
				default:
					paging = PageRequest.of(currentPage, pageSize);
					break;
				}
			} else {
				paging = PageRequest.of(currentPage, pageSize);
			}

			Page<ProjectEntity> pageTuts;
			if (nameProject == null) {
				pageTuts = projectRepository.findAll(paging);
			} else {
				pageTuts = projectRepository.findByNameProjectContaining(nameProject.trim(), paging);
			}

			projectEntities = pageTuts.getContent();

			if (projectEntities.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			Map<String, Object> response = new HashMap<>();
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			response.put("project", projectConverter.toDTOs(projectEntities));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
