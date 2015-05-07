#!/bin/bash

# Run starterupper over the network (use for deployment, not testing)
# Configuration
# ---------------------------------------------------------------------

# The course repository, aka upstream, is at:
# https://UPSTREAM_HOST/UPSTREAM_USER/REPO

# The course repository project host (Pick only one below)
readonly UPSTREAM_HOST=github.com
#readonly UPSTREAM_HOST=gitlab.com
#readonly UPSTREAM_HOST=bitbucket.org

# The course repository username (i.e., the instructor username)
readonly UPSTREAM_USER=lawrancej

# The instructor's user name at each project host
# (comment out if you have no account on that host)
readonly INSTRUCTOR_BITBUCKET=lawrancej
readonly INSTRUCTOR_GITHUB=lawrancej
readonly INSTRUCTOR_GITLAB=lawrancej

# The course repository name
# Hint: subject-number-year-semester (NO SPACES ALLOWED)
readonly REPO=COMP603-2015

# The domain of your school
# (Used to guess student school email addresses)
readonly SCHOOL=wit.edu

# Run starter upper over the network: curl http://path/to/main.sh | bash
# ---------------------------------------------------------------------

# Wherever we are, go home
cd ~
# Download starter upper
curl -L https://github.com/lawrancej/starterupper/archive/master.zip 2> /dev/null > starterupper.zip
# Extract
unzip -o starterupper.zip > /dev/null 2>&1
# Move into hidden folder
rm -rf .starterupper
mv starterupper-master .starterupper
# Clean up zip file
rm starterupper.zip
# Make starter upper executable
chmod +x .starterupper/starter-upper.sh
# Import starter upper
. .starterupper/starter-upper.sh
# Run
starterupper::main
