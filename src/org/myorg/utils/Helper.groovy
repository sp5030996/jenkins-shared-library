package org.myorg.utils

class Helper implements Serializable {

    def steps

    Helper(steps) {
        this.steps = steps
    }

    void logInfo(String message) {
        steps.echo "[INFO] ${message}"
    }

    void logError(String message) {
        steps.echo "[ERROR] ${message}"
    }

    String sanitizeBranchName(String branchName) {
        return branchName.replaceAll('[^a-zA-Z0-9_-]', '-')
    }
}
