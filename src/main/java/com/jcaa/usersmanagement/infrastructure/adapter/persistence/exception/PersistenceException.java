package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save user with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update user with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find user with ID: '%s'.";
  private static final String MESSAGE_EMAIL = "Failed to find user with email: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all users.";
  private static final String MESSAGE_DELETE = "Failed to delete user with ID: '%s'.";
  // EMPRESA
  private static final String MESSAGE_EMPRESA_SAVE = "Failed to save empresa with ID: '%s'.";
  private static final String MESSAGE_EMPRESA_UPDATE_ANNUAL_BILLING = "Failed to update annual billing of empresa with ID: '%s'.";
  private static final String MESSAGE_EMPRESA_CHANGE_SEDE = "Failed to change sede of empresa with ID: '%s'.";
  private static final String MESSAGE_EMPRESA_CHANGE_SECTOR = "Failed to change sector of empresa with ID: '%s'.";
  private static final String MESSAGE_EMPRESA_FIND = "Failed to find empresa with ID: '%s'.";
  private static final String MESSAGE_EMPRESA_NAME = "Failed to find empresa with name: '%s'.";
  private static final String MESSAGE_EMPRESA_ALL = "Failed to retrieve all empresas.";
  private static final String MESSAGE_EMPRESA_DELETE = "Failed to delete empresa with ID: '%s'.";


  private static final String MESSAGE_CONNECTION = "Could not establish database connection.";

 private PersistenceException(final String message, final Throwable cause) {
   super(message, cause);
 }

  // ==========================
  // USER
  // ==========================

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
      final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMAIL, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL, cause);
  }

  public static PersistenceException becauseDeleteFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE, userId), cause);
  }

  // ==========================
  // EMPRESA
  // ==========================

  public static PersistenceException becauseEmpresaSaveFailed(final String idEmpresa, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMPRESA_SAVE, idEmpresa), cause);
  }

    public static PersistenceException becauseEmpresaUpdateAnnualBillingFailed(final String idEmpresa, final Throwable cause) {
        return new PersistenceException(String.format(MESSAGE_EMPRESA_UPDATE_ANNUAL_BILLING, idEmpresa), cause
        );
    }

    public static PersistenceException becauseEmpresaChangeSedeFailed(final String idEmpresa, final Throwable cause) {
        return new PersistenceException(String.format(MESSAGE_EMPRESA_CHANGE_SEDE, idEmpresa), cause
        );
    }

    public static PersistenceException becauseEmpresaChangeSectorFailed(final String idEmpresa, final Throwable cause) {
        return new PersistenceException(String.format(MESSAGE_EMPRESA_CHANGE_SECTOR, idEmpresa), cause
        );
    }

    public static PersistenceException becauseFindEmpresaByIdFailed(final String idEmpresa, final Throwable cause) {
        return new PersistenceException(String.format(MESSAGE_EMPRESA_FIND, idEmpresa), cause);
    }

    public static PersistenceException becauseFindEmpresaByNameFailed(final String empresaName, final Throwable cause) {
        return new PersistenceException(String.format(MESSAGE_EMPRESA_NAME, empresaName), cause);
    }

  public static PersistenceException becauseFindAllEmpresasFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_EMPRESA_ALL, cause);
  }

  public static PersistenceException becauseEmpresaDeleteFailed(final String idEmpresa, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMPRESA_DELETE, idEmpresa), cause
    );
  }

  // ==========================
  // CONNECTION
  // ==========================

  public static PersistenceException becauseConnectionFailed(
          final Throwable cause) {return new PersistenceException(MESSAGE_CONNECTION, cause);
  }
}